import sys

def try_count(l, n):
    if not l:
        return sys.maxsize
    
    count = 0
    
    for i in range(len(l) - 1):
        if l[i] + 1 != l[i + 1]:
            count += 1
    
    if l[0] != 0:
        count += 1
    
    if l[-1] != n - 1:
        count += 1
    
    return count

