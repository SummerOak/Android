package casetest;

/**
 * 本地修改&云端修改   有冲突
 * 
 * last: ABC
 * local: BAC
 * cloud: ACB
 * expect: BAC or ACB
 * 
 * 
 * @author chedifier
 *
 */
public class BookmarkCase2 extends TestCase{

	@Override
	public void prepareCase() {
		caseData.add(new TestBookmarkBuilder("A")
				.dirty(0)
				.local_next("C")
				.cloud_next("C")
				.order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("B")
				.dirty(0)
				.local_next("A")
				.order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("C")
				.dirty(0)
				.cloud_next("B")
				.order_time(1L)
				.build());
	}

}
