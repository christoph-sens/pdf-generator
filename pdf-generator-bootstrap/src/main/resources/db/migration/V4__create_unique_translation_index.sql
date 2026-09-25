-- Uploads used to append translations; keep only the most recent entry per key before enforcing uniqueness.
DELETE FROM translation older
USING translation newer
WHERE older.template_name = newer.template_name
  AND older.country_code = newer.country_code
  AND older.language_code = newer.language_code
  AND older.name = newer.name
  AND older.id < newer.id;

CREATE UNIQUE INDEX unique_index_translation ON translation (template_name, country_code, language_code, name);
