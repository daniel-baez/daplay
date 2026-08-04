{
  description = "daplay - Jekyll site dev environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = { self, nixpkgs }:
    let
      supportedSystems = [
        "x86_64-linux"
        "aarch64-linux"
        "x86_64-darwin"
        "aarch64-darwin"
      ];
      forAllSystems = nixpkgs.lib.genAttrs supportedSystems;
      pkgsFor = system: import nixpkgs { inherit system; };

      # `serve` installs gems (if needed) and runs the Jekyll dev server.
      serveFor = pkgs: pkgs.writeShellApplication {
        name = "serve";
        runtimeInputs = [ pkgs.ruby pkgs.bundler pkgs.gcc pkgs.gnumake ];
        text = ''
          # Keep gems local to the repo so nothing is installed globally.
          export BUNDLE_PATH="''${BUNDLE_PATH:-vendor/bundle}"
          if ! bundle check >/dev/null 2>&1; then
            echo "Installing gems into $BUNDLE_PATH ..."
            bundle install
          fi
          exec bundle exec jekyll serve --host 0.0.0.0 "$@"
        '';
      };
    in
    {
      devShells = forAllSystems (system:
        let
          pkgs = pkgsFor system;
          serve = serveFor pkgs;
        in
        {
          default = pkgs.mkShell {
            packages = [
              pkgs.ruby
              pkgs.bundler
              pkgs.gcc
              pkgs.gnumake
              pkgs.git
              serve
            ];

            # Gems live in ./vendor/bundle (gitignored) instead of globally.
            BUNDLE_PATH = "vendor/bundle";

            shellHook = ''
              echo "daplay Jekyll dev shell ready."
              echo "  serve [jekyll args]   # bundle install (if needed) + jekyll serve on 0.0.0.0:4000"
              echo "  e.g. serve --livereload"
            '';
          };
        });

      # `nix run` starts the Jekyll dev server (installs gems on first run).
      apps = forAllSystems (system:
        let
          pkgs = pkgsFor system;
          serve = serveFor pkgs;
        in {
          default = {
            type = "app";
            program = "${serve}/bin/serve";
          };
        });
    };
}
