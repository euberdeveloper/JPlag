import sys

def try_count(a, n):
    if not a:
        return sys.maxsize

    count = 0

    for i in range(len(a) - 1):
        if a[i] + 1 != a[i + 1]:
            count += 1

    if a[0] != 0:
        count += 1

    if a[-1] != n - 1:
        count += 1

    return count

