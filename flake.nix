{
  description = "daplay - Jekyll + ClojureScript (shadow-cljs) site";

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

      # Install gems + npm deps as needed, compile CLJS, run Jekyll.
      serveFor = pkgs: pkgs.writeShellApplication {
        name = "serve";
        runtimeInputs = [
          pkgs.ruby
          pkgs.bundler
          pkgs.gcc
          pkgs.gnumake
          pkgs.nodejs
          pkgs.jdk21
        ];
        text = ''
          export BUNDLE_PATH="''${BUNDLE_PATH:-vendor/bundle}"

          if ! bundle check >/dev/null 2>&1; then
            echo "Installing gems into $BUNDLE_PATH ..."
            bundle install
          fi

          if [[ ! -d node_modules ]]; then
            echo "Installing npm packages ..."
            npm install
          fi

          echo "Compiling ClojureScript (shadow-cljs) ..."
          npx shadow-cljs compile app

          exec bundle exec jekyll serve --host 0.0.0.0 "$@"
        '';
      };

      buildFor = pkgs: pkgs.writeShellApplication {
        name = "build-site";
        runtimeInputs = [
          pkgs.ruby
          pkgs.bundler
          pkgs.gcc
          pkgs.gnumake
          pkgs.nodejs
          pkgs.jdk21
        ];
        text = ''
          export BUNDLE_PATH="''${BUNDLE_PATH:-vendor/bundle}"
          bundle check >/dev/null 2>&1 || bundle install
          [[ -d node_modules ]] || npm install
          npx shadow-cljs release app
          bundle exec jekyll build "$@"
        '';
      };

      testFor = pkgs: pkgs.writeShellApplication {
        name = "test-site";
        runtimeInputs = [
          pkgs.nodejs
          pkgs.jdk21
        ];
        text = ''
          [[ -d node_modules ]] || npm install
          npm test
        '';
      };
    in
    {
      devShells = forAllSystems (system:
        let
          pkgs = pkgsFor system;
          serve = serveFor pkgs;
          build-site = buildFor pkgs;
          test-site = testFor pkgs;
        in
        {
          default = pkgs.mkShell {
            packages = [
              pkgs.ruby
              pkgs.bundler
              pkgs.gcc
              pkgs.gnumake
              pkgs.git
              pkgs.nodejs
              pkgs.jdk21
              serve
              build-site
              test-site
            ];

            BUNDLE_PATH = "vendor/bundle";

            shellHook = ''
              echo "daplay Jekyll + ClojureScript shell ready."
              echo "  serve [jekyll args]   # npm/bundle if needed + cljs compile + jekyll serve"
              echo "  build-site           # shadow-cljs release + jekyll build"
              echo "  test-site            # shadow-cljs node tests"
              echo "  npm run cljs:watch   # iterative CLJS rebuild (separate terminal)"
            '';
          };
        });

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
