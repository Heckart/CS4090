BGreen='\033[1;32m'
BIRed='\033[1;91m'
BIPurple='\033[1;35m'
NC='\033[0m'

echo -e "${BIPurple}Checking formatting..\n"
cmp -s "${FILE}" formatted_file
for FILE in **/*.cpp; do
    TEMP_FILE=$(mktemp)

    clang-format --style=LLVM "$FILE" > "$TEMP_FILE"

    if ! diff -q "$FILE" "$TEMP_FILE" >/dev/null 2>&1; then
	echo -e "${BIRed}'$FILE' is not properly formatted."
	echo "Run 'clang-format --style=LLVM -i $FILE' to fix the formatting."
    fi

    rm "$TEMP_FILE"
done

for FILE in **/*.h; do
    TEMP_FILE=$(mktemp)

    clang-format --style=LLVM "$FILE" > "$TEMP_FILE"

    if ! diff -q "$FILE" "$TEMP_FILE" >/dev/null 2>&1; then
	echo -e "${BIRed}'$FILE' is not properly formatted."
	echo "Run 'clang-format --style=LLVM -i $FILE' to fix the formatting."
    fi

    rm "$TEMP_FILE"
done

echo -e "${BIPurple}Done.\n"

echo -e "${BIPurple}Running static analysis suite...\n"


echo -e "${NC}Running cppcheck static analysis..."
cppcheck -v --platform=unix64 --std=c++20 --output-file=cppcheck_report.txt **/. > /dev/null # report should be blank if passed
if [ -s cppcheck_report.txt ]; then
    echo -e "${BIRed}Issues found by cppcheck."
    cat cppcheck_report.txt
else
    echo -e "${BGreen}cppcheck static analysis passed."
    rm cppcheck_report.txt
fi

echo -e "${NC}Running flawfinder static analysis..."
flawfinder --minlevel=1 **/. > flawfinder_report.txt # report should include "no hits found" if passed
if grep -Fq "No hits found" flawfinder_report.txt; then
    echo -e "${BGreen}flawfinder static analysis passed."
    rm flawfinder_report.txt
else
    echo -e "${BIRed}Issues found by flawfinder."
    cat flawfinder_report.txt
fi

echo -e "${NC}Running lizard static analysis..."
lizard -V -o lizard_report.txt **/. # report should include "No thresholds exceeded" if passed
if grep -Fq "No thresholds exceeded" lizard_report.txt; then
    echo -e "${BGreen}lizard static analysis passed."
    rm lizard_report.txt
else
    echo -e "${BIRed}Issued found by lizard"
    cat lizard_report.txt
fi

echo -e "${NC}Running scan-build static analysis..."
scan-build g++ -g3  src/*.cpp -o api_backend -lcpprest -lboost_system -lssl -lcrypto -lpthread > scan-build_report.txt # report should include "No bugs found" if passed
if grep -Fq "No bugs found" scan-build_report.txt; then
    echo -e "${BGreen}scan-build static analysis passed."
    rm scan-build_report.txt
    rm api_backend
else
    echo -e "${BIRed}Issues found by scan-build."
    cat scan-build_report.txt
fi
