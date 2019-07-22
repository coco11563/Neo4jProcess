package pub.sha0w.neo4j.`object`.set

import pub.sha0w.neo4j.`object`.pNode

import scala.collection.mutable

class NodeSet {
  val ns : mutable.HashSet[pNode] = new mutable.HashSet[pNode]()
  val categoriesSet : mutable.HashSet[String] = new mutable.HashSet[String]()
  override def toString: String = {
    if (ns.isEmpty) return ""
    ns.map(p => {
      p.toString
    }).reduce(_ + "," + _)
  }
  def addNode(node : pNode) :Unit  = {
    if (!ns.contains(node)) {
      ns.add(node)
      if (!categoriesSet.contains(node.categories))
        categoriesSet.add(node.categories)
    }
  }
  def getByCat(cat : String): Seq[pNode] = {
    ns.filter(p => p.categories == cat).toSeq
  }
  def getIdByCat (cat : String) : Seq[Long] = {
    getByCat(cat).map(p => p.id)
  }
}
