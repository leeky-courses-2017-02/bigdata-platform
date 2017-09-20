def sumInts(a: Int, b: Int): Int = if (a > b) 0 else a + sumInts(a + 1, b)

def square(a: Int): Int = x * x
def sumSquares(a: Int, b: Int): Int = if (a > b) 0 else square(a) + sumSquares(a+1, b)

def powerOfTwo(x: Int): Int = if(x == 0) 1 else 2 * powerOfTwo(x-1)
def sumPowersOfTwo(a: Int, b: Int): Int = if (a > b) 0 else powerOfTwo(a) + sumPowersOfTwo(a+1, b)

def sum(f: Int => Int, a: Int, b: Int): Int = if (a > b) 0 else f(a) + sum(f, a+1, b)

def id(x: Int): Int = x
def square(a: Int): Int = x * x
def powerOfTwo(x: Int): Int = if(x == 0) 1 else 2 * powerOfTwo(x-1)

def sumInts(a: Int, b: Int): Int = sum(id, a, b)
def sumSquares(a: Int, b: Int): Int = sum(square, a, b)
def sumPowersOfTwo(a: Int, b: Int): Int = sum(powerOfTwo, a, b)

def sumInts(a: Int, b: Int): Int = sum((x: Int) => x, a, b)
def sumSquares(a: Int, b:Int): Int = sum((x: Int) => x * x, a, b)

