# Stash raw Markdown before Jekyll converts it to HTML so ClojureScript
# can own the rendering pipeline. Available in Liquid as page/post.raw_markdown.
Jekyll::Hooks.register [:posts, :pages], :pre_render do |doc|
  doc.data["raw_markdown"] = doc.content.dup
end
