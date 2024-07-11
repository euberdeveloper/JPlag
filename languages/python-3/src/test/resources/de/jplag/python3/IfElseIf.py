import sys

if __name__ == "__main__":
    if len(sys.argv) == 1:
        raise ValueError("No command-line arguments provided.")
    else:
        print(sys.argv[1:])

