class ChecksumAccumulator {
  var publicSum = 0
  private var sum = 0

  def add(b: Byte): Unit = {
    sum += b
  }

  def checksum(): Int = {
    return ~(sum &0xFF) + 1
  }
}

class Person(var firstName:String, val lastName: String, age: Int) {
  override def toString = firstName + " " + lastName + " " + age
}

object DateUtils {
  def getCurrentDate:String = "2017-09-01"
}

DateUtils.getCurrentDate

