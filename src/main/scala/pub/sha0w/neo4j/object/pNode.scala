package pub.sha0w.neo4j.`object`

import org.neo4j.driver.types.Node
import pub.sha0w.neo4j.`object`.subclass.SubNode

import scala.collection.mutable

trait pNode {
  val label : String
  val image : String = "NaN"
  val categories : String
  val valueMap : mutable.HashMap[String, Any] = new mutable.HashMap[String, Any]()
  val id : Long
  override def hashCode(): Int = id.toInt

  override def equals(obj: Any): Boolean = {
    if (obj == null) false
    if (!obj.isInstanceOf[Relation]) false
    val otherEdge = obj.asInstanceOf[Relation]
    if (otherEdge.hashCode() == this.hashCode()) true else false
  }
  override def toString: String = {
    s"""
       |{
       | "id" : $id,
       | "label" : "$label", ${if (image == "NaN") "" else "\n\"image\" :" + image + ",\n"}
       | "categories" : [
       |    "$categories"
       | ],
      """.stripMargin + valueMap.keySet.map(str => {
      "\"" + str + "\"" + " : " + {
        val s = valueMap(str)
        s match {
          case str1: String =>
            "\"" + str1 + "\""
          case li: List[String] =>
            "[" + li.map(str => "\"" + str + "\"").reduce(_ + "," + _) + "]"
          case _ => "\"\""
        }
      }
    }).reduce(_ + ",\n" + _) + "}"
  }
}
object pNode {
  def apply(node : Node, cat:String): pNode = {
    cat match {
      case "TaxonNode" => new TaxonNode(node)
      case "TaxonName" => new TaxonName(node)
      case "ProteinNode" => new ProteinNode(node)
      case "GeneNode" => new GeneNode(node)
      case "GenomeNode" => new GenomeNode(node)
      case _ => new SubNode(node, cat)
    }
  }
}
