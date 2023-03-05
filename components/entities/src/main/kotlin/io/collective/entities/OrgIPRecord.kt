package io.collective.entities

import java.math.BigInteger

data class OrgIPRecord(val id: Long, val startIP: BigInteger, val endIP: BigInteger, val orgId: Long)