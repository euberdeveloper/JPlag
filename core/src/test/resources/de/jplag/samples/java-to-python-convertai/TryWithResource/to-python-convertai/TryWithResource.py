import os

def load(path):
    try:
        with open(path, 'r') as scanner:
            for line in scanner:
                print(line.strip())
    except FileNotFoundError:
        print(f"Error: {path} not found.")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    load("DoesNotExist.txt")

