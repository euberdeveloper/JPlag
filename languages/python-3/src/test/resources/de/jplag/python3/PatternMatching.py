from dataclasses import dataclass

@dataclass
class Test:
    x: int

def test():
    a = Test(1)
    if isinstance(a, Test):
        testA = a

