package com.dgg.receiptprocessorchallenge.persistance.postgres

import cats.effect.IO
import cats.~>
import doobie.ConnectionIO

trait PostgresImplicits
    extends doobie.free.Instances
    with doobie.syntax.AllSyntax
    with doobie.util.meta.MetaConstructors
    with doobie.util.meta.SqlMetaInstances
    with doobie.util.meta.TimeMeta
    with doobie.util.meta.LegacyMeta
    with doobie.postgres.Instances
    with doobie.postgres.free.Instances
    with doobie.postgres.syntax.ToPostgresMonadErrorOps
    with doobie.postgres.syntax.ToFragmentOps
    with doobie.postgres.syntax.ToPostgresExplainOps {}

object PostgresImplicits extends PostgresImplicits {

  def liftIO[A](io: IO[A])(implicit lifter: IO ~> ConnectionIO): ConnectionIO[A] = lifter(io)

}
