package ru.tinkoff.petstore.commons

import cats.effect.kernel.Async
import org.typelevel.log4cats.Logger
import retry.RetryDetails.{GivingUp, WillDelayAndRetry}
import retry.{RetryDetails, RetryPolicies, RetryPolicy}
import ru.tinkoff.petstore.commons.configuration.RetryConfiguration

trait RetryUtils[F[_]] {
  def onError(error: Throwable, retryDetails: RetryDetails): F[Unit]
  def policy: RetryPolicy[F]
}

class RetryUtilsImpl[F[_]: Async](logger: Logger[F], retryConfiguration: RetryConfiguration)
    extends RetryUtils[F] {
  def policy: RetryPolicy[F] = RetryPolicies.constantDelay[F](retryConfiguration.retryDuration)
  def onError(error: Throwable, retryDetails: RetryDetails): F[Unit] = retryDetails match {
    case WillDelayAndRetry(_, retriesSoFar, _) if retriesSoFar <= retryConfiguration.amount =>
      logger.info(s"Failed to download with $error. So far we have retried $retriesSoFar times.")
    case GivingUp(totalRetries, _) =>
      logger.error(s"Giving up with $error after $totalRetries retries")
  }
}
