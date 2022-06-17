from rest_framework.permissions import BasePermission


class PostPremissions(BasePermission):
    def has_permission(self, request, view):
        if request.method == 'POST':
            return request.user.is_authenticated
        return True

    def has_object_permission(self, request, view, obj):
        if request.method == 'PUT':
            return obj.author == request.user or request.user.is_staff
        elif request.method == 'DELETE':
            return obj.author == request.user or request.user.is_staff
