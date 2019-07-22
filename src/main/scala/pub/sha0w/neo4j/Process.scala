package pub.sha0w.neo4j

import org.neo4j.kernel.GraphDatabaseQueryService
import pub.sha0w.neo4j.`object`.{Relation, TaxonName, pNode}
import pub.sha0w.neo4j.`object`.set.{DataSet, NodeSet, RelSet}
import pub.sha0w.neo4j.`object`.subclass.SubNode
import pub.sha0w.neo4j.utils.Neo4jConnect
import pub.sha0w.neo4j.utils.Neo4jConnect.{passwd, startQuey, url, user}

object Process {
  val url = "bolt://10.0.88.65:7687"
  val user = "neo4j"
  val passwd = "bigdata"
  val startName = "562"
  val startQuey = s"""match (p : tax {tax_id : "$startName"}) return p"""
//  val q1: String => String = (str :String) => s"""match (p : TaxonName ) where id(p) = $str return p """
  val queryRel: (pNode, String, String, Int) => String =
    (node : pNode, relName : String, retCat : String, limit :Int) =>
    s"""match (p : ${node.categories} )-[:$relName]-(q:$retCat) where id(p) = ${node.id} return q limit $limit""".stripMargin

  val queryRelNoLimit: (pNode, String, String) => String =
    (node : pNode, relName : String, retCat : String) =>
      s"""match (p : ${node.categories} )-[:$relName]-(q:$retCat) where id(p) = ${node.id} return q""".stripMargin


  def main(args: Array[String]): Unit = {
    val ns = new NodeSet
    val rs = new RelSet
    val ds = new DataSet(ns, rs)
    val conn = new Neo4jConnect(url, user, passwd)
    val ret = conn.cypher(startQuey)
    var subNode : SubNode = null
    while (ret.hasNext) {
      val rec = ret.next().get(0).asNode()
      subNode = new SubNode(rec,"tax")
      ds.addNode(subNode)
    }
    ds.nodeSet.getByCat("tax").
      foreach(queryStartByNode(_, "tax_genome", "genome", ds, conn, 100))

    ds.nodeSet.getByCat("tax").
      foreach(queryStartByNode(_, "tax_gene", "gene", ds, conn, 100))

    ds.nodeSet.getByCat("tax").
      foreach(queryStartByNode(_, "tax_uniprot", "uniprot", ds, conn, 100))

    println(ds.toString)
    conn.close()
  }

  def queryStartByNode (no : pNode, relName : String, cat : String, ds: DataSet, conn : Neo4jConnect): Unit = {
    val query = queryRelNoLimit(no, "`" + relName + "`", cat)
    println(query)
    val ret2 = conn.cypher(query)
    while (ret2.hasNext) {
      val rec2 = ret2.next()
      for (i <- 0 until rec2.size()) {
        val taxonNode = pNode(rec2.get(i).asNode(), cat)
        ds.addNode(taxonNode)
        ds.addRel(new Relation(no.id, taxonNode.id, relName))
      }
    }
  }
  def queryStartByNode (no : pNode, relName : String, cat : String, ds: DataSet, conn : Neo4jConnect, limit : Int): Unit = {
    val query = queryRel(no, "`" + relName + "`", cat, limit)
    println(query)
    val ret2 = conn.cypher(query)
    while (ret2.hasNext) {
      val rec2 = ret2.next()
      for (i <- 0 until rec2.size()) {
        val taxonNode = pNode(rec2.get(i).asNode(), cat)
        ds.addNode(taxonNode)
        ds.addRel(new Relation(no.id, taxonNode.id, relName))
      }
    }
  }
}
