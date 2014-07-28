package git

import org.joda.time.{Minutes, DateTime}

case class PullRequest(number: Int,
                       author: String,
                       sha: String,
                       source: String,
                       target: String,
                       title: String,
                       createdAt: DateTime,
                       mergedAt: Option[DateTime],
                       closedAt: DateTime)
{
  // Time dependent features
  var linesAdded: Long = 0L
  var linesDeleted: Long = 0L
  var filesChanged: Long = 0L
  var commits: Long = 0L
  var comments: Long = 0L
  var reviewComments: Long = 0L
  var coreMember: Boolean = false
  var contributedCommitRatio: Double = 0D
  var pullRequestAcceptRatio: Double = 0D

  def age(now: DateTime): Long = Minutes.minutesBetween(createdAt, now).getMinutes
}
