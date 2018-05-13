package com.github.simplesteph.ksm.grpc

import com.github.simplesteph.ksm.AclSynchronizer
import com.security.kafka.pb.ksm.KsmServiceGrpc
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.{Server, ServerBuilder}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

class KsmGrpcServer(aclSynchronizer: AclSynchronizer,
                    port: Int,
                    enabled: Boolean) {

  val log = LoggerFactory.getLogger(KsmServiceGrpc.getClass)

  private[this] var server: Server = _

  def start(): Unit = {
    if (enabled) {
      log.info("Starting gRPC Server")
      server = ServerBuilder
        .forPort(port)
        .addService(ProtoReflectionService.newInstance())
        .addService(
          KsmServiceGrpc.bindService(new KsmServiceImpl(aclSynchronizer),
                                     ExecutionContext.global))
        .build()
      server.start()
      log.info("gRPC Server Started")
    }

  }

  def stop(): Unit = {
    if (enabled) {
      server.shutdown()
    }
  }

}
