package pub.sha0w.neo4j.`object`

class Relation (startId : Long, endId : Long, relName : String) extends Edge {
  override val from: Long = startId
  override val to: Long = endId
  override val label: String = relName
}
object Relation {
}
