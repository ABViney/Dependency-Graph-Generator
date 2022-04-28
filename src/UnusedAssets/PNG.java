package UnusedAssets;

public class PNG {
	
	byte[] header = { (byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A };
	
	PNG(int w, int h, int bpp) {
		// header makes up first 8 bytes, rest of file is png data
		
		//chunks
		
		//length
		// 4-byte unsigned int, number of bytes in this chunk's data field. Counts only the data length, not itself, the chunk-type code, or the CRC. can be zero
		// Value is always treated unsigned, but cannot exceed 231 bytes
		
		//chunk type
		// 4-byte chunk-type code. restricted to [A-Za-z] ASCII (65-90 | 97-122)
		//naming conventions
		
		//chunk data
		//data bytes appropriate to chunk type. Can be zero length
		
		//CRC
		// 4-byte (Cyclic Redundancy Check) calculated on the preceeding bytes in the chunk, including chunk type code and chunk data fields, but not length
		// always present, even if there's no chunk data
		
		//Chunk types
		//Categorized into Critical or Ancillary based on 4-byte case-sensitive ASCII chunk-type code.
		//Valid PNG contains IHDR chunk, 1+ IDAT chunks, and an IEND chunk
		//First byte - Capital=Critical, Lower=Ancillary
		//Second byte - Capital=Public, Lower=Private
		//Third byte - Capital -- PNG specification, reserved byte. Lowercase isn't allowed
		//Fourth byte - Capital=Only copy if modifications have not touched critical chunks, Lowercase=Safe to copy
		
		//Critical chunks
			//First chunk - IHDR chunk
		//width - 4 bytes
		//height - 4 bytes
		//bit depth - 1 byte (1, 2, 4, 8, 16)
		//color type - 1 byte (0, 2, 3, 4, 6)
		//compression method - 1 byte (0)
		//filter method - 1 byte (0)
		//interlace method 1 byte (0 "no interlace", 1 "Adam7 interlace" )
		
			//1 - n-1 chunks
		//PLTE -- contains palette - list of colors
		//IDAT == contains image data, can be multiple IDAT chunks. Multiple splits increases filesize but enables generating in a streaming manner.
		//IEND marks image end and is empty
		//PLTE -- in relation to color type -- (3 - required), (2, 6 - optional), (0, 4 - do not use)
		
		// Thinking run-length encoding would be most efficient with relatively good space 
		
		
	}
}
