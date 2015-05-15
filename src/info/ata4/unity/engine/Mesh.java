/*
 ** 2014 July 10
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.unity.engine;

import info.ata4.log.LogUtils;
import info.ata4.unity.engine.struct.Vector2f;
import info.ata4.unity.engine.struct.Vector3f;
import info.ata4.unity.serdes.UnityObject;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

// Mesh (Unity 4)
//   string m_Name
//   vector m_SubMeshes
//   BlendShapeData m_Shapes
//   vector m_BindPose
//   vector m_BoneNameHashes
//   unsigned int m_RootBoneNameHash
//   UInt8 m_MeshCompression
//   UInt8 m_StreamCompression
//   bool m_IsReadable
//   bool m_KeepVertices
//   bool m_KeepIndices
//   vector m_IndexBuffer
//   vector m_Skin
//   VertexData m_VertexData
//   CompressedMesh m_CompressedMesh
//   AABB m_LocalAABB
//   int m_MeshUsageFlags

// Mesh (Unity 3)
//   string m_Name
//   vector m_SubMeshes
//   UInt8 m_MeshCompression
//   vector m_IndexBuffer
//   vector m_Skin
//   vector m_BindPose
//   VertexData m_VertexData
//   CompressedMesh m_CompressedMesh
//   AABB m_LocalAABB
//   int m_MeshUsageFlags

public class Mesh {
    
    public final String name;
    public final ByteBuffer indexBuffer;
    public final Integer meshCompression;
    public final VertexData vertexData;
    public final ArrayList<Vector3f> vertices;
    public final ArrayList<Vector3f> normals;
    public final ArrayList<Vector2f> uvs;
    public final List<SubMesh> subMeshes;
    public final CompressedMesh compressedMesh;

    public Mesh(UnityObject obj) {
        name = obj.getValue("m_Name");
        indexBuffer = obj.getValue("m_IndexBuffer");
        meshCompression = obj.getValue("m_MeshCompression");
        VertexData vd = null;
        
        try{
            vd = obj.getObject("m_VertexData", VertexData.class);
        }catch(Exception ex){
            
        }
        
        vertexData = vd;
        List<UnityObject> verts = null;
        List<UnityObject> norms = null;
        List<UnityObject> uv = null;
        
        if(vertexData == null){
            try{
                verts = obj.getValue("m_Vertices");
                norms = obj.getValue("m_Normals");
                uv = obj.getValue("m_UV");
            }catch(Exception ex){
                LogUtils.getLogger().log(Level.WARNING, "There is no mesh data");
            }
        }
        
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        uvs = new ArrayList<>();
        
        if(verts != null){
            for(UnityObject vertex : verts){
                Vector3f vert = new Vector3f();
                vert.x = vertex.getValue("x");
                vert.y = vertex.getValue("y");
                vert.z = vertex.getValue("z");
                vertices.add(vert);
            }
        }
        
        if(norms != null){
            for(UnityObject normal : norms){
                Vector3f vert = new Vector3f();
                vert.x = normal.getValue("x");
                vert.y = normal.getValue("y");
                vert.z = normal.getValue("z");
                normals.add(vert);
            }
        }
        
        if(uv != null){
            for(UnityObject vt : uv){
                Vector2f vert = new Vector2f();
                vert.x = vt.getValue("x");
                vert.y = vt.getValue("y");
                uvs.add(vert);
            }
        }
        
        
        
        List<UnityObject> subMeshObjects = obj.getValue("m_SubMeshes");
        subMeshes = new ArrayList<>();
        for (UnityObject subMeshObject : subMeshObjects) {
            subMeshes.add(new SubMesh(subMeshObject));
        }
        compressedMesh = obj.getObject("m_CompressedMesh", CompressedMesh.class);
    }
    
}
