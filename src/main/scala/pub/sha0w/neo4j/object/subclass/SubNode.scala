package pub.sha0w.neo4j.`object`.subclass

import org.neo4j.driver.internal.value.{ListValue, StringValue}
import org.neo4j.driver.types.Node
import pub.sha0w.neo4j.`object`.pNode

class SubNode(node : Node, tlabel : String) extends pNode{
  override val label: String = node.get("tax_id").asString("")
  override val categories: String = tlabel
  override val id: Long = node.id()
  this.valueMap.addAll(scala.jdk.CollectionConverters.
    MapHasAsScala(
      node.asMap {f => f match {
        case f : ListValue => f.asList(j => j.asString(""))
        case f : StringValue => f.asString("")
        case _ => ""
      }
      }
    ).asScala
    .filter(pair => pair._1 != "tax_id"))
}
