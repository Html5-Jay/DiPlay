# Building DiPlay

Requirements: JDK 25, Android SDK 37, NDK 28.2.13676358 and the included Gradle wrapper.

## GitHub Actions release build

The `Android release` workflow builds and uploads only a signed Release APK. It runs after a push to any branch, for pull requests, or when started manually from the Actions page. It does not build or upload a Debug APK.

Configure these repository Actions secrets before running it:

- `DIPLAY_IDENTITY_PK8_B64`: Base64-encoded `identity.pk8` used for runtime CarPlay authentication.
- `DIPLAY_CERTIFICATE_P7B_B64`: Base64-encoded `certificate.p7b` used for runtime CarPlay authentication.
- `ANDROID_KEYSTORE_B64`: Base64-encoded Android Release keystore.
- `ANDROID_KEYSTORE_PASSWORD`: Keystore password.
- `ANDROID_KEY_ALIAS`: Signing-key alias.
- `ANDROID_KEY_PASSWORD`: Signing-key password.

The workflow reconstructs these files under the temporary runner directory, builds `:mobile:assembleRelease`, uploads the APK as the `diplay-release-apk` artifact, and removes the temporary credential files afterward. If any secret is missing, the workflow fails instead of producing an incomplete APK.

Keep a secure backup of the keystore and its passwords. Every future update to the same installed app must be signed by the same key. Do not commit the authentication files, keystore, passwords, or encoded secret values to Git.

## Optional local Release packaging

Provide an external asset directory using `DIPLAY_AUTH_ASSETS_DIR`. It must contain `offline-mfi/identity.pk8` and `offline-mfi/certificate.p7b`. Set `ANDROID_KEYSTORE_PATH`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, and `ANDROID_KEY_PASSWORD` for the Android signing key.

```sh
./gradlew :shared:testReleaseUnitTest :common:testReleaseUnitTest :mobile:lintRelease :mobile:assembleRelease
```

Output: `mobile/build/outputs/apk/release/mobile-release.apk`. The Release APK deliberately contains the experimental identity described in the notices, so recipients can extract it. The Android signing key is not included in the APK.

The public source archive excludes runtime identities, signing keys, local configuration, APKs, and build output.
