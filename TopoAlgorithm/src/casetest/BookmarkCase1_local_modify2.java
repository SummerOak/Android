package casetest;

/**
 * ±¾µØÐÞ¸Ä
 * 
 * last: ABCD		1
 * local: DCBA		2
 * 
 * expect: DCBA
 * 
 * @author chedifier
 *
 */
public class BookmarkCase1_local_modify2 extends TestCase{

	@Override
	public void prepareCase() {
		caseData.add(new TestBookmarkBuilder("A")
				.dirty(0)
				.local_next(null)
				.order_time(2L)
				.cloud_next("B")
				.cloud_order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("B")
				.dirty(0)
				.local_next("A")
				.order_time(2L)
				.cloud_next("C")
				.cloud_order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("C")
				.dirty(0)
				.local_next("B")
				.order_time(2L)
				.cloud_next("D")
				.cloud_order_time(1L)
				.build());
		caseData.add(new TestBookmarkBuilder("D")
				.dirty(0)
				.local_next("C")
				.order_time(2L)
				.cloud_next(null)
				.cloud_order_time(1L)
				.build());
	}

}
