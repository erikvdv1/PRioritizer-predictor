package ghtorrent

import com.mongodb._
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

class MongoDatabase(host: String, port: Int, username: String, password: String, databaseName: String) {
  private var client: MongoClient = _
  private var database: DB = _
  private var connected: Boolean = _

  def isOpen: Boolean = connected

  def open(): MongoDatabase = {
    if (connected)
      return this

    val server = new ServerAddress(host, port)
    client = if (username != null && username.nonEmpty) {
      val credential = MongoCredential.createMongoCRCredential(username, databaseName, password.toCharArray)
      new MongoClient(server, java.util.Arrays.asList(credential))
    } else {
      new MongoClient(server)
    }

    client.setReadPreference(ReadPreference.secondaryPreferred())
    database = client.getDB(databaseName)
    connected = true

    this
  }

  def getById(collectionName: String, objectId: String, select: List[String]) : Map[String, Any] = {
    if (objectId == "")
      return Map()

    val query = MongoDBObject("_id" -> new ObjectId(objectId))

    val fields = new BasicDBObject()
    select.foreach(f => fields.put(f, 1))

    val collection = database.getCollection(collectionName)
    val result = collection.findOne(query, fields)
    select
      .map(f => getField(result, f).map(v => (f, v)))
      .flatten
      .toMap
  }

  def getBySha(collectionName: String, sha: String, select: List[String]) : Map[String, Any] = {
    if (sha == "")
      return Map()

    val query = MongoDBObject("sha" -> sha)

    val fields = new BasicDBObject()
    select.foreach(f => fields.put(f, 1))

    val collection = database.getCollection(collectionName)
    val result = collection.findOne(query, fields)
    select
      .map(f => getField(result, f).map(v => (f, v)))
      .flatten
      .toMap
  }

  private def getField(obj: DBObject, fullPath: String): Option[Any] = {
    def iteration(x: Any, path: Array[String]): Option[Any] = {
      x match {
        case l: BasicDBList => Some(l.toArray.toList.map(e => iteration(e, path)))
        case o: DBObject => iteration(o.get(path.head), path.tail)
        case s: String => Some(s)
        case i: Int => Some(i)
        case _ => None
      }
    }
    iteration(obj, fullPath.split("""\."""))
  }

  def close(): Unit = {
    connected = false
    if (client != null)
      client.close()
  }
}
