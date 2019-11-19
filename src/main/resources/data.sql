
INSERT INTO public.pattern_language (id, name, uri, logo) VALUES ('82146836-1f69-4f8d-81c5-3d87a8db7663', 'Cloud Computing Patterns', 'https://patternpedia.org/patternlanguages/cloudcomputingpatterns', 'https://www.cloudcomputingpatterns.org/img/book.png');
INSERT INTO public.pattern_language (id, name, uri, logo) VALUES ('82146836-1f69-4f8d-81c5-3d87a8db7664', 'Enterprise Integration Patterns', 'https://patternpedia.org/patternlanguages/enterpriseintegrationpatterns', 'https://www.enterpriseintegrationpatterns.com/img/0321200683.gif');
INSERT INTO public.pattern_view (id, name, uri) VALUES ('82146836-1f69-4f8d-81c5-3d87a8db7690', 'TestView', 'https://patternpedia.org/patternViews/TestView');
INSERT INTO public.pattern_view (id, name, uri) VALUES ('82146836-1f69-4f8d-81c5-3d87a8db7691', 'TestView2', 'https://patternpedia.org/patternViews/TestView2');
INSERT INTO public.pattern (id, name, uri, content, pattern_language_id) VALUES ('82146836-1f69-4f8d-81c5-3d87a8db7665', 'TestPattern1', 'http://patternpedia.org/TestPattern1', '{"a": "b"}', '82146836-1f69-4f8d-81c5-3d87a8db7663');
INSERT INTO public.pattern (id, name, uri, content, pattern_language_id) VALUES ('82146836-1f69-4f8d-81c5-3d87a8db7666', 'TestPattern2', 'http://patternpedia.org/TestPattern2', '{"c": "d"}', '82146836-1f69-4f8d-81c5-3d87a8db7663');
INSERT INTO public.pattern_view_patterns (pattern_view_id, pattern_id) VALUES ('82146836-1f69-4f8d-81c5-3d87a8db7690', '82146836-1f69-4f8d-81c5-3d87a8db7666');
INSERT INTO public.pattern_view_patterns (pattern_view_id, pattern_id) VALUES ('82146836-1f69-4f8d-81c5-3d87a8db7691', '82146836-1f69-4f8d-81c5-3d87a8db7666');
