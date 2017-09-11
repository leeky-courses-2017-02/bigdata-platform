val list = List(1, 2, 3)
val list = List.range(1, 10)
val list = List.fill(3)("big data")
val list = 1 :: 2 :: 3 :: Nil

list.head
list head
list.tail

import scala.language.postfixOps
list tail
list.isEmpty

list(2)
list.last
list.length

var sum = 0
def sumList1 (myList: List[Int]) {
  for (i <- 0 until myList.length) {
    sum += myList(i)
  }
}

def sumList2 (myList: List[Int]) {
  if (!myList.isEmpty) {
    sum += myList.head
    sumList2(myList tail)
  }
}

