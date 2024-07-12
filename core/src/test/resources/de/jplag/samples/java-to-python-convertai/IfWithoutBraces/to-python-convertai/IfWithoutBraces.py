import sys

if not sys.argv[1:]:
    raise ValueError("Illegal argument")
elif len(sys.argv) > 2:
    print(str(sys.argv[1:]))
else:
    print(sys.argv[1])

