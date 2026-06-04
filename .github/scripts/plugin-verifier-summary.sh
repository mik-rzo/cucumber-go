#!/usr/bin/env bash
# Compile the IntelliJ Plugin Verifier per-IDE reports into a single Markdown
# overview, written to stdout (the workflow redirects it to $GITHUB_STEP_SUMMARY).
#
# Usage: plugin-verifier-summary.sh <reports-dir>
#
# <reports-dir> holds one subdirectory per verified IDE build (e.g. GO-262.6653.43,
# IU-261.25134.95), each containing the verifier's *.txt report files under
# plugins/<pluginId>/<version>/. Category files only exist when non-empty.
#
# Category labels and severity tiers mirror the IntelliJ Platform Gradle Plugin's
# VerifyPluginTask.FailureLevel enum, so new IDEs flagging a previously-unseen
# category are surfaced automatically without changing this script.
set -euo pipefail

reportsDir=${1:?usage: plugin-verifier-summary.sh <reports-dir>}

# One row per FailureLevel: "files|tier|emoji|label". Order = render order; "files"
# is a space-separated list of the verifier report files that map to that level
# (INTERNAL_API_USAGES is reported across two files). tier is problem/warning (used
# for the roll-up counts). The 🔴 problem tier matches the levels that fail the
# verifyPlugin task under the default failureLevel ({COMPATIBILITY_PROBLEMS,
# INTERNAL_API_USAGES, OVERRIDE_ONLY_API_USAGES}) plus the unconditional
# invalid-plugin check; everything else is a 🟡 warning.
categories=(
  "compatibility-problems.txt|problem|🔴|Compatibility problems"
  "invalid-plugin.txt|problem|🔴|Invalid plugin"
  "internal-api-usages.txt internal-api-kt-usages.txt|problem|🔴|Internal API usages"
  "override-only-usages.txt|problem|🔴|Override-only API usages"
  "compatibility-warnings.txt|warning|🟡|Compatibility warnings"
  "plugin-structure-warnings.txt|warning|🟡|Plugin structure warnings"
  "deprecated-usages.txt|warning|🟡|Deprecated API usages"
  "experimental-api-usages.txt|warning|🟡|Experimental API usages"
  "non-extendable-api-usages.txt|warning|🟡|Non-extendable API usages"
)

# Extract the IDE build id (GO-… / IU-…) from a report file path.
ideOf() { grep -oE '(GO|IU)-[0-9.]+' <<<"$1" | head -1; }

# Pluralize: <count> <singular> -> "1 usage" / "2 usages".
plural() { [ "$1" = "1" ] && echo "$1 $2" || echo "$1 ${2}s"; }

# ---- Verdict table (one row per IDE build) ----
verdictRows=""
buildCount=0
while IFS= read -r verdictFile; do
  ide=$(ideOf "$verdictFile")
  verdict=$(head -1 "$verdictFile" | tr -d '\r')
  verdict=${verdict//|/\\|}
  verdictRows+="| $ide | $verdict |"$'\n'
  buildCount=$((buildCount + 1))
done < <(find "$reportsDir" -name verification-verdict.txt | sort)

# ---- Category sections (only those with content) ----
declare -A tierCount=( [problem]=0 [warning]=0 )
sections=""
for entry in "${categories[@]}"; do
  IFS='|' read -r files tier emoji label <<<"$entry"

  # Build a find expression matching any of the category's report files.
  findArgs=()
  for fn in $files; do findArgs+=( -name "$fn" -o ); done
  unset 'findArgs[${#findArgs[@]}-1]'

  # Map each unique finding to the sorted set of IDE builds that reported it.
  declare -A findingIdes=()
  order=()
  while IFS= read -r catFile; do
    ide=$(ideOf "$catFile")
    while IFS= read -r line; do
      [ -n "$line" ] || continue
      line=${line//$'\r'/}
      if [ -z "${findingIdes[$line]+x}" ]; then
        order+=("$line")
        findingIdes[$line]="$ide"
      else
        findingIdes[$line]+=" $ide"
      fi
    done <"$catFile"
  done < <(find "$reportsDir" \( "${findArgs[@]}" \) | sort)

  [ ${#order[@]} -eq 0 ] && { unset findingIdes; continue; }

  tierCount[$tier]=$(( ${tierCount[$tier]} + ${#order[@]} ))
  sections+="### $emoji $label"$'\n\n'
  for finding in "${order[@]}"; do
    ides=$(tr ' ' '\n' <<<"${findingIdes[$finding]}" | sort -u | paste -sd ',' - | sed 's/,/, /g')
    sections+="- $finding _(${ides})_"$'\n'
  done
  sections+=$'\n'
  unset findingIdes
done

# ---- Roll-up headline ----
p=${tierCount[problem]}; w=${tierCount[warning]}
scope="Verified $(plural "$buildCount" "IDE build")"
if [ "$p" -eq 0 ] && [ "$w" -eq 0 ]; then
  rollup="✅ ${scope} — no problems or warnings"
else
  probSeg=$([ "$p" -gt 0 ] && echo "🔴 $(plural "$p" problem)" || echo "✅ no problems")
  warnSeg=$([ "$w" -gt 0 ] && echo "🟡 $(plural "$w" warning)" || echo "✅ no warnings")
  rollup="${scope} — ${probSeg} · ${warnSeg}"
fi

# ---- Emit ----
echo "## Plugin Verifier results"
echo ""
echo "$rollup"
echo ""
echo "| IDE build | Verdict |"
echo "| --- | --- |"
printf '%s' "$verdictRows"
echo ""
printf '%s' "$sections"