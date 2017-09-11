def last[T](xs: List[T]): T = xs match {
  case List() => throw new Error("an empty list")
  case List(x) => x
  case y :: ys => last(ys)
}

