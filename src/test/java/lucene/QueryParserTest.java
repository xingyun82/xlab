package lucene;

import com.google.common.collect.Sets;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.util.Set;

public class QueryParserTest {

    private Set<String> preservedFields = Sets.newHashSet("explicit", "clean", "live", "single", "testMode", "ignoreIds", "includeKaraoke");


    private String queryToString(Query luceneQuery, Set<String> preservedFields) {
        String field = null;
        String value = null;
        if(luceneQuery instanceof TermQuery) {
            field = ((TermQuery)luceneQuery).getTerm().field();
            value = ((TermQuery)luceneQuery).getTerm().text();
        } else if(luceneQuery instanceof PhraseQuery) {
            Term[] terms = ((PhraseQuery)luceneQuery).getTerms();
            field = terms[0].field().toLowerCase();
            StringBuilder sb = new StringBuilder();
            for(Term term : terms){
                sb.append(term.text()).append(" ");
            }
            value = sb.toString();
        }
        if (!preservedFields.contains(field)) {
            return null;
        }
        return field + ":\"" + value + "\"";
    }

    @Test
    public void test() throws Exception {
        StringBuilder sb = new StringBuilder();
        String filters = "artist:\"taylor swift\" AND explicit:true AND ignoreIds:\"123466,1511\"";
        QueryParser luceneParser = new QueryParser(Version.LUCENE_31, "",new WhitespaceAnalyzer(Version.LUCENE_31));
        Query luceneQuery = luceneParser.parse(filters);
        if (luceneQuery instanceof BooleanQuery) {
            for (BooleanClause bClause: ((BooleanQuery)luceneQuery).clauses()) {
                Query q = bClause.getQuery();
                String filterQuery = queryToString(q, preservedFields);
                if(filterQuery != null){
                    if(sb.length() > 1){
                        if(bClause.getOccur().equals(BooleanClause.Occur.SHOULD)) {
                            sb.append(" OR ");
                        } else if(bClause.getOccur().equals(BooleanClause.Occur.MUST)) {
                            sb.append(" AND ");
                        } else if(bClause.getOccur().equals(BooleanClause.Occur.MUST_NOT)) {
                            sb.append(" AND NOT ");
                        }
                    }
                    sb.append(filterQuery);
                }
            }
        } else {
            String filterQuery = queryToString(luceneQuery, preservedFields);
            if (filterQuery != null) {
                sb.append(filterQuery);
            }
        }
        System.out.println(sb.toString());
    }
}
