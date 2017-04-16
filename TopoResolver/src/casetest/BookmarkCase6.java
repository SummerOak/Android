package casetest;

/**
 * ÔÆ¶ËÊý¾Ý´íÂÒ£ºÔÆ¶ËÐÞ¸Ä£¬´æÔÚ»·
 * 
 * last: ABCD
 * cloud:
 * 			D->A;
 * 
 * local:   ABCD
 * 
 * expect: 	CDBA or DCBA
 * 
 * @author wxj_pc
 *
 */
public class BookmarkCase6 extends TestCase{

	@Override
	public void prepareCase() {
		caseData.add(new TestBookmarkBuilder("A")
				.dirty(0)
				.local_next("B")
				.cloud_next("B")
				.order_time(2L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("B")
				.dirty(0)
				.local_next("C")
				.cloud_next("C")
				.order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("C")
				.dirty(0)
				.local_next("D")
				.cloud_next(null)
				.order_time(1L)
				.build());
		caseData.add(new TestBookmarkBuilder("D")
				.dirty(0)
				.local_next(null)
				.cloud_next("A")
				.order_time(3L)
				.build());
	
	}

}