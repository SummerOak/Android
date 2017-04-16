package casetest;


/**
 * 云端数据错乱：云端新增，存在环
 * 
 * last: AB
 * cloud:
 * 			A->B ; 
 * 			B->NULL;
 * 		    C->D;
 * 			D->C;
 * 
 * local:   B -> A
 * 
 * expect: 	CDBA or DCBA
 * 
 * @author wxj_pc
 *
 */
public class BookmarkCase5 extends TestCase{

	@Override
	public void prepareCase() {
		caseData.add(new TestBookmarkBuilder("A")
				.dirty(0)
				.local_next(null)
				.cloud_next("B")
				.order_time(2L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("B")
				.dirty(0)
				.local_next("A")
				.cloud_next(null)
				.order_time(1L)
				.build());
		
		caseData.add(new TestBookmarkBuilder("C")
				.dirty(0)
				.local_next(null)
				.cloud_next("D")
				.order_time(1L)
				.build());
		caseData.add(new TestBookmarkBuilder("D")
				.dirty(0)
				.local_next(null)
				.cloud_next("C")
				.order_time(1L)
				.build());
	}

}