package pub.sha0w.neo4j.`object`

import org.neo4j.driver.internal.value.{ListValue, StringValue}
import org.neo4j.driver.types.Node

import scala.collection.mutable

class TaxonNode (node : Node) extends pNode {
  override val label: String = node.get("ENTITY_ID").asString("")
  this.valueMap.addAll(scala.jdk.CollectionConverters.
    MapHasAsScala(
      node.asMap {f => f match {
        case f : ListValue => f.asList(j => j.asString(""))
        case f : StringValue => f.asString("")
        case _ => ""
      }
      }
    ).asScala
    .filter(pair => pair._1 != "ENTITY_ID"))
  override val id: Long = node.id()
  override val categories: String = "TaxonNode"
}
