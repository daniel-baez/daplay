{
  description = "daplay - static Bootstrap site dev environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = { self, nixpkgs }:
    let
      # Systems we support the dev shell on.
      supportedSystems = [
        "x86_64-linux"
        "aarch64-linux"
        "x86_64-darwin"
        "aarch64-darwin"
      ];
      forAllSystems = nixpkgs.lib.genAttrs supportedSystems;
      pkgsFor = system: import nixpkgs { inherit system; };
    in
    {
      devShells = forAllSystems (system:
        let
          pkgs = pkgsFor system;

          # `serve` starts a local static file server for the site.
          serve = pkgs.writeShellScriptBin "serve" ''
            port="''${1:-8000}"
            echo "Serving $(pwd) at http://localhost:$port/ (Ctrl-C to stop)"
            exec ${pkgs.python3}/bin/python3 -m http.server "$port"
          '';
        in
        {
          default = pkgs.mkShell {
            packages = [
              pkgs.python3
              pkgs.git
              serve
            ];

            shellHook = ''
              echo "daplay dev shell ready."
              echo "  serve [port]   # serve the static site (default port 8000)"
            '';
          };
        });

      # Convenience: `nix run` starts the static server.
      apps = forAllSystems (system:
        let pkgs = pkgsFor system;
        in {
          default = {
            type = "app";
            program = "${pkgs.writeShellScript "serve-app" ''
              exec ${pkgs.python3}/bin/python3 -m http.server "''${1:-8000}"
            ''}";
          };
        });
    };
}
