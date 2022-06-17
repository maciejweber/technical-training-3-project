from rest_framework.permissions import BasePermission


class PostPremissions(BasePermission):
    def has_permission(self, request, view):
        if request.method == 'GET':
            return True
        elif request.method == 'POST':
            return request.user.is_authenticated
        elif request.method == 'PUT':
            return True
        elif request.method == 'DELETE':
            return True
        return False

    def has_object_permission(self, request, view, obj):
        if request.method == 'PUT':
            return obj.author == request.user or request.user.is_staff
        elif request.method == 'DELETE':
            return obj.author == request.user or request.user.is_staff
