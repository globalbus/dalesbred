## x.y.z (yyyy-mm-dd)

  - Changed the way that Isolation and Propagation are configured, so that the
    default isolation and propagation can be specified at Database-level even
    when using annotations.
  - Support for converting Transactional-annotation into TransactionSettings.
  - Support passing Provider<Database> instead of Database when creating TransactionalTests.

## 0.2.4 (2012-11-05)

  - Support for user defined type-conversions.
  - Optional Guice-support.
  - Optional support for AOP Alliance interceptors.
  - Bug fix: release InitialContext properly when looking up DataSource by JNDI-name.
  - Various small improvements.
  - Added this changelog.