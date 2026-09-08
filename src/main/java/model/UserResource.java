package model;

import jakarta.inject.Named;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Named
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    // Danh sách User lưu giữ trong bộ nhớ đóng vai trò cơ sở dữ liệu tạm thời
    private static final List<User> userList = new ArrayList<>();

    static {
        // Khởi tạo dữ liệu mẫu
        userList.add(new User(1, "Nguyen Van A", "a@gmail.com"));
        userList.add(new User(2, "Tran Thi B", "b@gmail.com"));
        userList.add(new User(3, "Duong Van Anh", "vananh@gmail.com"));
    }

    /**
     * HTTP GET: Lấy danh sách tất cả User
     * URL: GET /api/user/view hoặc GET /api/user
     */
    @GET
    @Path("/view")
    public Response getAllUsersView() {
        return Response.ok(userList).build();
    }

    @GET
    public Response getAllUsers() {
        return Response.ok(userList).build();
    }

    /**
     * HTTP GET: Lấy thông tin chi tiết của một User cụ thể theo ID (tham số)
     * URL: GET /api/user/{id} hoặc GET /api/user/view/{id}
     */
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") int id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return Response.ok(user).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("User with ID " + id + " not found")
                .build();
    }

    @GET
    @Path("/view/{id}")
    public Response getUserByIdInViewPath(@PathParam("id") int id) {
        return getUserById(id);
    }

    /**
     * HTTP POST: Tạo mới một User vào hệ thống
     * URL: POST /api/user
     */
    @POST
    public Response addUser(User newUser) {
        if (newUser == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dữ liệu User không hợp lệ")
                    .build();
        }
        userList.add(newUser);
        return Response.status(Response.Status.CREATED)
                .entity(newUser)
                .build();
    }

    /**
     * HTTP PUT: Cập nhật toàn diện một User đã tồn tại (sửa thông tin cá nhân)
     * URL: PUT /api/user/{id} hoặc PUT /api/user
     */
    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") int id, User updatedUser) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId() == id) {
                updatedUser.setId(id);
                userList.set(i, updatedUser);
                return Response.ok(updatedUser).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("User với ID " + id + " không tồn tại")
                .build();
    }

    @PUT
    public Response updateUserWithoutPathId(User updatedUser) {
        if (updatedUser == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return updateUser(updatedUser.getId(), updatedUser);
    }

    /**
     * HTTP DELETE: Xóa bỏ một User khỏi hệ thống theo ID
     * URL: DELETE /api/user/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") int id) {
        boolean removed = userList.removeIf(user -> user.getId() == id);
        if (removed) {
            return Response.ok("Đã xóa User có ID " + id + " thành công").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User với ID " + id + " không tồn tại")
                    .build();
        }
    }
}