# iam-oidc-client

IAM OIDC Client is a lightweight, web-based OpenID Connect client designed to test, debug, and explore OAuth 2.0 and OpenID Connect integrations with INDIGO IAM and other compatible identity providers.

## Key features

- Dynamic OpenID Connect provider discovery.
- Authorization Code Flow with PKCE.
- Support for both public and confidential clients.
- Customizable issuer and requested scopes through the web interface.
- Visualization of access, ID, and refresh tokens, including decoded JWT contents.
- UserInfo endpoint integration.
- Token introspection support.
- Configurable client settings through environment variables.
- Optional hiding of raw token values.

## Technology stack

- Java 21
- Spring Boot 4.2
- Spring Security
- Thymeleaf

## Docker distribution

Multi-architecture Docker images are available on [Docker Hub](https://hub.docker.com/r/indigoiam/iam-oidc-client), supporting both `linux/amd64` and `linux/arm64`.

```bash
docker pull indigoiam/iam-oidc-client:v0.1.0
```

To start the application:

```bash
docker run --rm -p 9090:9090 \
  -e IAM_CLIENT_ID=your-client-id \
  -e IAM_CLIENT_SECRET=your-client-secret \
  -e IAM_CLIENT_ISSUER=https://your-iam-instance \
  indigoiam/iam-oidc-client:v0.1.0
```

For public clients, set `IAM_CLIENT_SECRET` to an empty string.

The application is available at http://localhost:9090/iam-test-client/.

The container runs as a non-root user.

> **Note:** This application is intended for testing and debugging purposes. It should not be considered a production-ready relying party or exposed to untrusted users without appropriate security controls.

Feedback, bug reports, and contributions are welcome!
