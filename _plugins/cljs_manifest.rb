# Expose shadow-cljs assets/js/manifest.json as site.data["cljs"] so layouts
# can reference hashed release filenames (module-hash-names).
require "json"

module Daplay
  class CljsManifest < Jekyll::Generator
    safe true
    priority :high

    def generate(site)
      path = File.join(site.source, "assets", "js", "manifest.json")
      return unless File.file?(path)

      site.data["cljs"] = JSON.parse(File.read(path))
    end
  end
end
