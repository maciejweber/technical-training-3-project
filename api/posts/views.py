from django.http import Http404

from rest_framework import status, filters
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.generics import ListCreateAPIView


from .models import Post
from .serializers import PostDetailSerializer, PostSerializer
from .premissions import PostPremissions


class PostListView(ListCreateAPIView):
    permission_classes = [PostPremissions]
    serializer_class = PostSerializer
    filter_backends = [filters.SearchFilter]
    search_fields = ['company', 'title']

    def get_queryset(self):
        return Post.objects.all()

    def perform_create(self, serializer):
        serializer.save(author=self.request.user)


class PostDetailView(APIView):
    permission_classes = [PostPremissions]

    def get_object(self, pk):
        try:
            return Post.objects.get(pk=pk)
        except Post.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        post = self.get_object(pk)
        serializer = PostDetailSerializer(post)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        post = self.get_object(pk)
        self.check_object_permissions(request, post)
        serializer = PostDetailSerializer(post, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        post = self.get_object(pk)
        self.check_object_permissions(request, post)
        post.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
