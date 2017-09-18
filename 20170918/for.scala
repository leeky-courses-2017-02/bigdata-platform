var a = 0
for (a <- 1 to 10) {
  println("value of a: " + a)
}

for (a <- 1 until 10) {
  println("value of a: " + a)
}

val numList = List(1,2,3,4,5,6,7,8,9,10)
for (a <- numList) {
  println("value of a: " + a)
}

for (a <- numList
     if a != 3; if a < 8) {
   println("value of a:" + a)
}

val retVal = for {a <- numList if a != 3; if a < 8} yield a

for (a <- retVal) {
  println("value of a:" + a)
}



