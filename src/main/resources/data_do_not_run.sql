INSERT INTO patternpedia.public.pattern_language (id, name, uri, logo) VALUES
('82146836-1f69-4f8d-81c5-3d87a8db7663', 'Cloud Computing Patterns', 'https://patternpedia.org/patternlanguages/cloudcomputingpatterns', 'https://www.cloudcomputingpatterns.org/img/book.png'),
('82146836-1f69-4f8d-81c5-3d87a8db7664', 'Enterprise Integration Patterns', 'https://patternpedia.org/patternlanguages/enterpriseintegrationpatterns', 'https://www.enterpriseintegrationpatterns.com/img/0321200683.gif');

INSERT INTO pattern (id, name, uri) VALUES
('82146836-1f69-4f8d-81c5-3d87a8db7665', 'TestPattern1', 'http://patternpedia.org/TestPattern1'),
('82146836-1f69-4f8d-81c5-3d87a8db7666', 'TestPattern2', 'http://patternpedia.org/TestPattern2');

INSERT INTO pattern_language_patterns (pattern_language_id, pattern_id) VALUES
('82146836-1f69-4f8d-81c5-3d87a8db7663', '82146836-1f69-4f8d-81c5-3d87a8db7665'),
('82146836-1f69-4f8d-81c5-3d87a8db7663', '82146836-1f69-4f8d-81c5-3d87a8db7666');