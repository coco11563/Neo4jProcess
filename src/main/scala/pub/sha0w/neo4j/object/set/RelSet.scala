package pub.sha0w.neo4j.`object`.set

import pub.sha0w.neo4j.`object`.Relation

import scala.collection.mutable

class RelSet {
  val rs : mutable.HashSet[Relation] = new mutable.HashSet[Relation]()
  override def toString: String = {
    if (rs.isEmpty) return ""
    rs.map(p => {
      p.toString
    }).reduce(_ + "," + _)
  }
  def addRel(rel : Relation) : Unit  = {
    if (!rs.contains(rel)) rs.add(rel)
  }
}
