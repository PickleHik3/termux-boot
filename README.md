# Termux:Boot

[![Build status](https://github.com/PickleHik3/termux-boot/actions/workflows/github_action_build.yml/badge.svg)](https://github.com/PickleHik3/termux-boot/actions/workflows/github_action_build.yml)

A [Termux](https://termux.dev) add-on app to run programs at boot.

This is the [Termux Launcher](https://github.com/PickleHik3/termux-launcher) fork of
[termux/termux-boot](https://github.com/termux/termux-boot). It exists because a plugin only gets
permission to run your scripts when it is signed with the same key as the terminal app beside it
and joins that app's shared user, and the launcher ships under three package names. The releases
here are built against those three, from the same shared debug key the launcher builds use.

## Editions

One build of this app belongs to exactly one launcher edition. Installing the wrong one gives you
an app that starts, shows this page, and then silently runs nothing at boot.

| Launcher edition | Branch | This app | Release tag |
| --- | --- | --- | --- |
| Termux (`com.termux`) | `master` | `com.termux.boot` | `v0.8.1` |
| Nix (`com.termux.launcher.nix`) | `nix-pkg` | `com.termux.launcher.nix.boot` | `nix-v0.8.1` |
| VAJ (`io.vaj.tl`) | `io-vaj-package` | `io.vaj.tl.boot` | `v0.8.1-vaj` |

An edition branch changes three values at the top of `app/build.gradle` — the launcher package
name, the app label and the APK basename — and the release notes line in
`.github/workflows/github_release_build.yml`. Everything else is shared: the manifest takes the
shared user id and the label from manifest placeholders, and the code takes the service, action,
intent extra, URI scheme and boot script directory from `BuildConfig.TERMUX_PACKAGE_NAME`. Nothing
else in the tree may name a package. Features go on `master` and reach the editions by merging it.

Every package in a shared user id must target sdk 28 or lower, and Android remembers the highest
target sdk any member has ever declared. Raising `targetSdkVersion` here would stop the launcher
beside it from executing anything in its prefix, and reinstalling does not undo it.

## Installation

Download the APK for your launcher edition from
[Releases](https://github.com/PickleHik3/termux-boot/releases), or take a per-commit build from a
[Github Actions](https://github.com/PickleHik3/termux-boot/actions/workflows/github_action_build.yml)
run.

Upstream's F-Droid build (`com.termux.boot` signed by Termux) works only with an F-Droid Termux
install, not with any launcher edition. Signature keys of all offered builds are different: before
you switch installation source you have to uninstall the terminal app and every plugin beside it.

## Releasing

Dispatch `github_release_build.yml` on the edition's branch with the tag from the table above. It
builds the APK, writes `checksums-sha256.txt` and creates the release.

## How to use

1. Install the Termux:Boot app.
2. Start the Termux:Boot app once by clicking on its launcher icon. This allows the app to be run at boot.
3. Create the `~/.termux/boot/` directory.
4. Put scripts you want to execute inside the `~/.termux/boot/` directory. If there are multiple files, they will be executed in a sorted order.
5. Note that you may want to run `termux-wake-lock` as first thing if you want to ensure that the device is prevented from sleeping.

### Examples

To start an sshd server and prevent the device from sleeping at boot,
create the following file at `~/.termux/boot/start-sshd`:

```sh
#!/data/data/com.termux/files/usr/bin/sh
termux-wake-lock
sshd
```

(On the Nix and VAJ editions the shebang and paths carry that edition's package name instead of
`com.termux`; the app's own overview page shows the right one.)

To start
[termux-services](https://wiki.termux.com/wiki/Termux-services), which
in turn starts enabled services, you can put the following in
`~/.termux/boot/start-services`:

```sh
#!/data/data/com.termux/files/usr/bin/sh
termux-wake-lock
source /data/data/com.termux/files/usr/etc/profile.d/start-services.sh
```

## License

Released under [the GPLv3 license](https://www.gnu.org/licenses/gpl.html).
