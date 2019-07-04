package pub.sha0w.neo4j.`object`.set

import pub.sha0w.neo4j.`object`.{Relation, pNode}

class DataSet (val nodeSet : NodeSet, val relSet: RelSet) {
  val catMap: Map[String, String] = Map("TaxonName" -> "分类名", "TaxonNode" -> "分类点"
  ,"GeneNode" -> "基因" , "GenomeNode" -> "基因组", "ProteinNode" -> "蛋白质")
  override def toString: String = {
    s"""{"categories":{
    |       "TaxonName" : "分类名",
    |       "TaxonNode" : "分类点",
    |       "GeneNode" : "基因",
    |       "GenomeNode" : "基因组",
    |       "ProteinNode" : "蛋白质"
    |},
    |"data":{
      |"nodes":[${nodeSet.toString}],
      |"edges":[${relSet.toString}]
      |}}""".stripMargin
  }

  def addNode (p : pNode) : Unit = {
    nodeSet.addNode(p)
  }
  def addRel (r: Relation) : Unit = {
    relSet.addRel(r)
  }
}
