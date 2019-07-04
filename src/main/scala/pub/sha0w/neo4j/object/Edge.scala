package pub.sha0w.neo4j.`object`

trait Edge {
  val from : Long
  val to : Long
  val label : String
  override def hashCode(): Int = (from + to + label).hashCode
  override def equals(obj: Any): Boolean = {
    if (obj == null) false
    if (!obj.isInstanceOf[Relation]) false
    val otherEdge = obj.asInstanceOf[Relation]
    if (otherEdge.from == this.from &&
      otherEdge.to == this.to &&
      otherEdge.label == this.label) true else false
  }
  override def toString: String = {
    s"""
       |{
       | "id" : ${hashCode()},
       | "label" : "$label",
       | "from" : $from,
       | "to" : $to
       |}
    """.stripMargin
  }
}
