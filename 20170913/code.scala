val xs =  List(1, 2,3,4,5,6,7,8,9,10)

def init[T](xs: List[T]): List[T] = xs match {
  case List() => throw new Error("an empty list")
  case List(x) => List()
  case y :: ys => y :: init(ys)
}  

init(xs) 
val inputList = List(-1, 2, -3, -4, 5)

inputList.map(x => x*10)
inputList.map(_ * 10)
inputList.map(x => x % 2 == 0)
inputList.map(_ % 2 == 0)
inputList.map(10 * _ + 5)
inputList.map(x => 10 * x + 5)
inputList.map(x => x.abs)
inputList.map(x => x abs) 
inputList.map(_ abs)

val greeting = List("Hello".toList, "from".toList, "Scala".toList)
greeting.map(_.toList)   
greeting.flatMap(_ toList) 

inputList.filter (x => x > 0)
inputList filter (_ > 0) 
inputList filterNot(_ > 0) 

List
Set

import collection.mutable.Set
Set
