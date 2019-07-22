package pub.sha0w.neo4j.utils

import org.neo4j.driver._
import pub.sha0w.neo4j.`object`.set.{DataSet, NodeSet, RelSet}
import pub.sha0w.neo4j.`object`.{Relation, TaxonName, pNode}

class Neo4jConnect(uri : String, user : String, passwd : String) extends AutoCloseable{
  private val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, passwd))

  override def close(): Unit = driver.close()

  def cypher (query : String) : StatementResult = {
    driver.session().readTransaction[StatementResult]((tx: Transaction) => {
      tx.run(query)
    })
  }

  def printGreeting (message : String) : Unit = {
    val sess = driver.session()
    val greeting = sess.writeTransaction((tx: Transaction) => {
      val result = tx.run(s"CREATE (a:Greeting) " +
        s"SET a.message = $message" +
        " RETURN a.message + ', from node ' + id(a)")
      result.single().get(0).asString()
    })
    print(greeting)
  }
}
object Neo4jConnect {
  val url = "bolt://*.*.*.*:7687"
  val user = "neo4j"
  val passwd = "bigdata"
  val startName = "Escherichia coli"
  val startQuey = s"""match (p : TaxonName {taxname : "$startName"}) return p"""
  val q1: String => String = (str :String) => s"""match (p : TaxonName ) where id(p) = $str return p """
  val queryRel: (pNode, String, String, Int) => String = (node : pNode, relName : String, retCat : String, limit :Int) =>
    s"""match (p : ${node.categories} )-[:$relName]-(q:$retCat) where id(p) = ${node.id} return q limit $limit""".stripMargin
  def main(args: Array[String]): Unit = {
    val ns = new NodeSet
    val rs = new RelSet
    val ds = new DataSet(ns, rs)
    val conn = new Neo4jConnect(url, user, passwd)
    val ret = conn.cypher(startQuey)
    var taxonName : TaxonName = null


    while (ret.hasNext) {
      val rec = ret.next().get(0).asNode()
      taxonName = new TaxonName(rec)
      ds.addNode(taxonName)
    }
    ds.nodeSet.getByCat("TaxonName").
      foreach(queryStartByNode(_, "taxid", "TaxonNode", ds, conn))
    ds.nodeSet.getByCat("TaxonNode").
      foreach(queryStartByNode(_, "x-taxon", "GeneNode", ds, conn))
//    ds.nodeSet.getByCat("GeneNode").
//      foreach(queryStartByNode(_, "x-genome", "GenomeNode", ds, conn))
    ds.nodeSet.getByCat("TaxonNode").
      foreach(queryStartByNode(_, "x-taxon", "GenomeNode", ds, conn))
    ds.nodeSet.getByCat("GenomeNode").
      foreach(queryStartByNode(_, "x-genome", "GeneNode", ds, conn))
    ds.nodeSet.getByCat("TaxonNode").
      foreach(queryStartByNode(_, "x-taxon", "ProteinNode", ds, conn))
    println(ds.toString)
//    println(ds.nodeSet.ns.head.toString)
    //    val query = queryRel(taxonName.id.toString, "`taxid`", "TaxonNode")
//    println(query)
//    val ret2 = conn.cypher(query)
//    while (ret2.hasNext) {
//      val rec2 = ret2.next().get(0).asNode()
//      val taxonNode = new TaxonNode(rec2)
//      ds.addNode(taxonNode)
//      ds.addRel(new Relation(taxonName.id, taxonNode.id, "taxid"))
//    }
//    println(ds.toString)
    conn.close()
  }

  def queryStartByNode (no : pNode, relName : String, cat : String, ds: DataSet, conn : Neo4jConnect, limit : Int = 25): Unit = {
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
