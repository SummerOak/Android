package casetest;

/**
 * ÔÆ¶ËÉ¾³ý
 * last: ABC				1
 * local:ÎÞÐÞ¸Ä:		ABC		1
 * cloud: É¾³ýB: 		AC		2
 * expect: AC
 * 
 * 
 * @author chedifier
 *
 */
public class BookmarkCaseDel2 extends TestCase{

	@Override
	public void prepareCase() {
		caseData.add(new TestBookmarkBuilder("A")
				.dirty(0)
				.local_next("B")
				.order_time(1L)
				.cloud_next("C")
				.cloud_order_time(2L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("B")
				.dirty(0)
				.local_next("C")
				.order_time(1L)
				.cloud_next("C")
				.cloud_order_time(1L)
				.deleted(1)
				.build());
		
		caseData.add(new TestBookmarkBuilder("C")
				.dirty(0)
				.local_next(null)				
				.order_time(1L)
				.cloud_next(null)
				.cloud_order_time(1L)
				.build());
	}

}

