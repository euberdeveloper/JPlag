import sys

if len(sys.argv) == 1:
    raise ValueError("No command-line arguments provided.")
else:
    print(sys.argv[1:])

